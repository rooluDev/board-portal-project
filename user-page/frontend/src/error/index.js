import router from "@/router";

class ErrorCommand {
    constructor(error) {
        this.error = error;
    }

    execute() {
        throw new Error("error");
    }
}

export class ErrorCommandFactory {
    static createdCommand(error) {
        switch (error.response.data.code) {
            case 'A001':
                return new BoardNotFoundCommand(error);
            case 'A002':
                return new FileNotFoundCommand(error);
            case 'A003':
                return new MemberNotFoundCommand(error);
            case 'A004':
                return new ThumbnailNotFoundCommand(error);
            case 'A005':
                return new NotLoggedInCommand(error);
            case 'A006':
                return new NotMyBoardCommand(error);
            case 'A007':
                return new LoginFailCommand(error);
            case 'A008':
                return new IllegalFileDataCommand(error);
            case 'A009':
                return new IdDuplicateCommand(error);
            case 'A010':
                return new DownloadFailCommand(error);
            case 'A011':
                return new StorageFailCommand(error);
            case 'A012':
                return new ServerErrorCommand(error);
            case 'A013':
                return new BoardDataCommand(error);
            case 'A014':
                return new JoinFailCommand(error);
            default:
                return new DefaultErrorCommand(error);
        }
    }
}

class JoinFailCommand extends ErrorCommand {
    execute() {
        alert("회원 가입 실패");
        router.push({name: 'Join'})
    }
}

class BoardNotFoundCommand extends ErrorCommand {
    execute() {
        alert("존재하지 않은 게시물입니다.");
        router.push({name: 'Main'});
    }
}

class FileNotFoundCommand extends ErrorCommand {
    execute() {
        alert("파일이 존재하지 않습니다.");
        router.push({name: 'Main'});
    }
}

class MemberNotFoundCommand extends ErrorCommand {
    execute() {
        alert("존재하지 않은 회원입니다.");
        router.push({name: 'Main'});
    }
}

class ThumbnailNotFoundCommand extends ErrorCommand {
    execute() {
        alert("존재하지 않은 썸네일입니다.");
        router.push({name: 'Main'});
    }
}

class IllegalFileDataCommand extends ErrorCommand {
    execute() {
        console.error(this.error.response.data.message);
        router.push({name: 'Error'});
    }
}

class DownloadFailCommand extends ErrorCommand {
    execute() {
        console.error(this.error.response.data.message);
        router.push({name: 'Error'});
    }
}

class StorageFailCommand extends ErrorCommand {
    execute() {
        console.error(this.error.response.data.message);
        router.push({name: 'Error'});
    }
}

class ServerErrorCommand extends ErrorCommand {
    execute() {
        console.error(this.error.response.data.message);
        router.push({name: 'Error'});
    }
}

class NotLoggedInCommand extends ErrorCommand {
    execute() {
        console.error(this.error.response.data.message);
        router.push({name: 'Error'});
    }
}

class NotMyBoardCommand extends ErrorCommand {
    execute() {
        console.error(this.error.response.data.message);
    }
}

class LoginFailCommand extends ErrorCommand {
    execute() {
        console.error(this.error.response.data.message);
    }
}

class IdDuplicateCommand extends ErrorCommand {
    execute() {
        console.error(this.error.response.data.message);
    }
}

class BoardDataCommand extends ErrorCommand {
    execute() {
        console.error(this.error.response.data.message);
    }
}

class DefaultErrorCommand extends ErrorCommand {
    execute() {
        console.log("No specific handler for this error code.");
    }
}